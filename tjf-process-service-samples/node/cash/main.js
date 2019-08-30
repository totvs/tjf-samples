var waitPort = require('wait-port');
var amqplib = require('amqplib');

// FRAMEWORK

function connect (options) {
  console.info("Connecting to: ", JSON.stringify(options));
  return amqplib.connect(options).then(function (conn) {
    return conn.createChannel();
  });
}

function bind (ch, exchange, queue) {
  return Promise.all([
    ch.assertQueue(queue),
    ch.assertExchange(exchange, "topic"),
    ch.bindQueue(queue, exchange, "#"),
    ch.consume(queue, handleMessage)
  ]);
}

function handleMessage (message) {
  console.info("[" + message.fields.exchange + "]", "<---------", message.properties.headers.type);
  var type = message.properties.headers.type || "undefined";
  if (typeof subscribers[type] != 'function') {
    console.error("No subscribers found to {}", type);
  }
  else {
    subscribers[type](JSON.parse(message.content.toString()));
  }
  channel.ack(message);
}

function publish (exchange, message) {
  console.log(message.header.type, "--------->", "[" + exchange + "]")
  channel.publish(exchange, '#', Buffer.from(JSON.stringify(message)), { "headers": { "type": message.header.type } });
}

// APPLICATION

var subscribers = {};

subscribers.NewRequestEvent = function (message) {
  publish("workflow-exchange", {
    "header": {
      "type": "RequestCreatedEvent",
      "tenantId": "tenant"
    },
    "content": {
      "employee": "John",
      "amount": 5000
    }
  });
};

subscribers.CaseTrueCommand = function (message) {
  message.header.type = "CaseTrueEvent";
  publish("workflow-exchange", message);
};

subscribers.CaseFalseCommand = function (message) {
  message.header.type = "CaseFalseEvent";
  publish("workflow-exchange", message);
};

subscribers.VerifyRequestCommand = function (message) {
  message.header.type = "VerifyRequestEvent";
  message.content.verified = 1;
  publish("workflow-exchange", message);
};

subscribers.RequestApprovalRequiredCommand = function (message) {
  message.header.type = "RequestApprovalRequiredEvent";
  publish("workflow-exchange", message);
};

subscribers.DepositCommand = function (message) {
  message.header.type = "DepositEvent";
  message.content.deposited = 1;
  publish("workflow-exchange", message);
};

var options = {
  protocol: "amqp",
  hostname: "rabbitmq",
  username: "guest",
  password: "guest"
};
var channel;

waitPort({ host: "process", port: 8080}, 20000).then((open) => {
  if (open) {
      connect(options).then(function (ch) {
        channel = ch;
        bind(ch, "domain-exchange", "requests").then(function (values) {
          publish("workflow-exchange", {
            "header": {
              "type": "CreateTenantCommand",
              "tenantId": "tenant"
            },
            "content": {}
          });
          setTimeout(function () {
            publish("domain-exchange", {
              "header": {
                "type": "NewRequestEvent",
                "tenantId": "tenant",
                "processInfo": {}
              },
              "content": {
                "employee": "John",
                "amount": 5000
              }
            });
          }, 5000);
        })
      })
      .catch(function (err) {
        console.error(err);
      });
  }
  else {
    console.error("PROCESS SERVER NOT FOUND !");
  }
});