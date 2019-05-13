package com.tjf.sample.github.securityweb.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tjf.sample.github.securityweb.domain.Machine;
import com.tjf.sample.github.securityweb.domain.Type;

@Component
public class MachineManager {

	private List<Machine> machines = new LinkedList<Machine>();
	
	public MachineManager(){
		machines.add(new Machine(Type.COLLECTOR));
		machines.add(new Machine(Type.CONVEYOR));
		machines.add(new Machine(Type.PRESS));
	}
	
	public void stop(int id) {
		machines.get(id).stop();
	}
	
	public void stopAll() {
		for(Machine machine:machines) {
			machine.stop();
		}
	}

	public void run(int id) {
		machines.get(id).run();
	}

	public List<Map<String, Object>> getMachines() {		
		List<Map<String, Object>> listMachines = new ArrayList<Map<String, Object>>();		
		for(int id = 0; id < machines.size();id++) {
			listMachines.add(getMachine(id));
		}		
		return listMachines;
	}
	
	public HashMap<String, Object> getMachine(int id) {		
		Machine machine = machines.get(id);		
		
		HashMap<String, Object> mapMachine = new HashMap<String, Object>();
	    mapMachine.put("id", id);
	    mapMachine.put("type", machine.getType());
	    mapMachine.put("running", machine.running());
	    
		return mapMachine; 		
	}
}
