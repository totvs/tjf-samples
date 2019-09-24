var amqplib = require('amqplib');
var fs = require('fs');

function show (filename, message) {
  fs.readFile(filename, function read(err, data) {
    console.log(data.toString());
    if (message) {
      console.log(message);
    }
  });
}

// MESSAGING FUNCTIONS

function connect (options) {
  console.info("Connecting to: ", JSON.stringify(options));
  return amqplib.connect(options).then(function (conn) {
    return conn.createChannel();
  });
}

function bind (ch, exchange, queue) {
  var queuename = exchange + "." + queue;
  return Promise.all([
    ch.assertQueue(queuename),
    ch.assertExchange(exchange, "topic"),
    ch.bindQueue(queuename, exchange, "#"),
    ch.consume(queuename, handleMessage)
  ]);
}

function handleMessage (message) {
  var content = message.content.toString();
  var type = message.properties.headers.type || "undefined";
  console.info("[" + message.fields.exchange + "]", "<---------", type);
  channel.ack(message);
  if (typeof subscribers[type] != 'function') {
    console.error("No subscribers found to {}", type);
  }
  else {
    subscribers[type](JSON.parse(content));
  }
}

function publish (exchange, message) {
  console.log(message.header.type, "--------->", "[" + exchange + "]")
  channel.publish(exchange, '#', Buffer.from(JSON.stringify(message)), { "headers": { "type": message.header.type } });
}

// MESSAGING SUBSCRIBERS
var subscribers = {};

subscribers.MarryWithPadmeCommand = function (message) {
  // The marriage location is just set by the tjf-process-server-sample
  // where using the tfj-process-server is it possible to set it in the
  // MarryWithPadmeCommand.
  // To have this information using the tjf-process-service-sample it
  // is necessary to send it with a subsequent event, in our case the
  // QuiGonFoundAnakinEvent
  show("images/marriage.txt", "At: " + message.content.place);
  message.header.type = "MarryWithPadmeEvent";
  message.content.killSandPeople = true;
  message.content.killMaceWindu = true;  
  setTimeout(function () { publish("workflow-exchange", message); }, 2000);
};

subscribers.BecomeDarthVaderCommand = function (message) {
  show("images/darthvader.txt");
  message.header.type = "BecomeDarthVaderEvent";
  setTimeout(function () { publish("workflow-exchange", message); }, 2000);
};

subscribers.VisitDeathStarCommand = function (message) {
  show("images/deathstar.txt");
  message.header.type = "VisitDeathStarEvent";
  setTimeout(function () { publish("workflow-exchange", message); }, 2000);
};

subscribers.RevealToLukeCommand = function (message) {
  show("images/iamyourfather.txt");
  message.header.type = "RevealToLukeEvent";
  setTimeout(function () { publish("workflow-exchange", message); }, 2000);
};

subscribers.FightWithLukeCommand = function (message) {
  show("images/fightwithluke.txt");
  message.header.type = "FightWithLukeEvent";
  setTimeout(function () { publish("workflow-exchange", message); }, 2000);
};

subscribers.EndProcessEvent = function (message) {
  show("images/theend.txt");
  setTimeout(function () { console.log("Press CTRL+C to finish !"); }, 2000);
};

var options = {
  protocol: "amqp",
  hostname: "localhost",
  username: "guest",
  password: "guest"
};
var channel;

show("images/tatooine.txt");
connect(options).then(function (ch) {
  channel = ch;
  bind(ch, "anakin-exchange", "messages").then(function (values) {
    publish("workflow-exchange", {
      "header": {
        "type": "CreateTenantCommand",
        "tenantId": "starwars"
      },
      "content": {}
    });
    setTimeout(function () {
      publish("workflow-exchange", {
        "header": {
          "type": "QuiGonFoundAnakinEvent",
          "tenantId": "starwars",
        },
        "content": {
          "message": "I wanna be a Jedi",
          // Uncomment the line below when using the tjf-process-service-sample
          // otherwise the marriage place will be not available in the
          // MarryWithPadmeCommand
          // place: "Varykino Lake Retreat"
          }
      });
    }, 4000);
  })
})
.catch(function (err) {
  console.error(err);
});