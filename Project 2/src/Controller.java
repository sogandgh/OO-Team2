import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;


@SuppressWarnings({"rawtypes", "unchecked"})
public class Controller {
	private UserInterface gui;
	private Processor processor;
	private List<Instruction> instructions;

	private DefaultListModel instructionModel;
	private DefaultListModel registerModel;
	private DefaultListModel memoryModel;

	private volatile boolean running = false;
	private boolean hexadecimal = false;

	public Controller() {
		gui = new UserInterface();
		gui.setGUIListener(listener);

		processor = new Processor();

		instructionModel = new DefaultListModel();
		gui.setInstructionListModel(instructionModel);

		registerModel = new DefaultListModel();
		gui.setRegisterListModel(registerModel);

		memoryModel = new DefaultListModel();
		gui.setMemoryListModel(memoryModel);
	}

	/**
	 * Refresh the interface with the current processor state
	 */
	private void refresh() {
		int pc = processor.getPcValue();
		///gui.setPc(pc);

		int instructionIndex = pc/4;
		if(instructionIndex >= instructionModel.getSize()) {
			gui.clearInstructionSelection();
		} else {
			gui.selectInstruction(instructionIndex);
		}

		registerModel.clear();
		memoryModel.clear();

		int[] registerData = processor.getRegisters();
		List<Integer> changedRegisters = processor.getChangedRegisters();
		for(int index : changedRegisters) {
			String repr = String.format(
					"%s: %s", RegisterFile.name(index), string_value(registerData[index]));
			registerModel.addElement(repr);
		}

		int[] memoryData = processor.getMemory();
		List<Integer> changedMemory = processor.getChangedMemory();
		for(int index : changedMemory) {
			String repr = String.format(
					"%s: %s", string_value((short)index), string_value(memoryData[index]));
			memoryModel.addElement(repr);
		}
	}

	private String string_value(int b) {
		if(hexadecimal) {
			return String.format("0x%x", b & 0xffffffffL);
		} else {
			return String.format("%d", b & 0xffffffffL);
		}
	}

	private void refreshLater() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	
	private void run() {
		if(running) {
			return;
		}
		new Thread(){
			@Override
			public void run() {
				if(running) {
					return;
				}

				running = true;
				
				while(running && !processor.isDone()) {

					step();
				}

				refreshLater();
			};
		}.start();
	}

	
	private void stop() {
		running = false;
	}

	
	private synchronized void step() {

		processor.step();
	}


	private void reset() {
		stop();
		step(); 
		processor.reset();
	}

	private void renderInstructions() {
		instructionModel.clear();
		for(Instruction i : instructions) {
			instructionModel.addElement(i.representation(hexadecimal));
		}
	}

	private void load(String filename) {
		String line;
		BufferedReader reader = null;
		instructions
		 = new ArrayList<Instruction>();

		instructionModel.clear();

		try {
			reader = new BufferedReader(new FileReader(filename));
			System.out.println("file opened");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			int i = 0;
			while((line = reader.readLine()) != null){

				i++;
				if(line.length() == 0) {
					continue;
				}
				try {
					Instruction instruction = new Instruction(line);
					instructions.add(instruction);
					System.out.println("new instruction: " + line);
				} catch (Exception e) {
					System.out.printf("Invalid instruction '%s' on line %d\n", line, i);
				}
			}
		} catch (IOException e) {
			System.out.printf("File reading error: %s \n", e.getMessage());
		}
		processor.setInstructionSet(instructions);
		renderInstructions();
		refresh();
	}

	private UserInterface.GUIListener listener = new UserInterface.GUIListener() {

		@Override
		public void onStop() {
			stop();
			refresh();
		}

		@Override
		public void onStep() {
			step();
			refresh();
		}

		@Override
		public void onRun() {
			run();
		}

		@Override
		public void onReset() {
			reset();
			refresh();
		}

		@Override
		public void onLoad(String filename) {
			load(filename);
		}

		@Override
		public void onHex() {
			hexadecimal = true;
			renderInstructions();
			refresh();
		}

		@Override
		public void onDec() {
			hexadecimal = false;
			renderInstructions();
			refresh();
		}
	};

	public static void main(String[] args) {
		new Controller();
	}
}
