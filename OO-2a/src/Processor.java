import java.util.ArrayList;
import java.util.List;


public class Processor {

	private ProgramCounter pc;

	private RegisterFile register;
	private MemoryFile memory;
	private ALU alu;

	private InstructionMemoryFile instructions;

	 
	public Processor() {
		System.out.println("Processor created");

		pc = new ProgramCounter();
		instructions = new InstructionMemoryFile();
		register = new RegisterFile();
		alu = new ALU();
		memory = new MemoryFile();
	}

	
	public void setInstructionSet(List<Instruction> instructions) {
		this.instructions.load(new ArrayList<Instruction>(instructions));
		reset();
	}


	public void reset() {
		pc.reset();
		register.reset();
		memory.reset();
	}

	
	public void step() {
		System.out.print("stepping");

		Instruction i;
		int alu_out = 0;
		boolean alu_zero = false;
		int data_out = 0;
		int write_data;
		int regData1 = 0;
		int regData2 = 0;
		int new_pc = pc.get();
		int branch_pc;

		if(isDone()) {
			return;
		}

		//Fetch
		i = instructions.fetch(pc);
		Control control = new Control(i);

		//Reg
		int writeReg = mux(i.getRt(), i.getRd(), control.isRegDist());
		register.setRegisters(i.getRs(), i.getRt(), writeReg);
		regData1 = register.readData1();
		regData2 = register.readData2();


		//Alu
		alu.setOperation(
				ALUControl.getControl(control.isALUOp1(), control.isALUOp0(), i.getFunct()),
				mux(regData2, 0, control.isALUsrc()),
				regData1);
		alu_out = alu.getOut();
		alu_zero = alu.isZero();

		//Mem
		data_out = memory.cycle(alu_out, regData2, control.isMemRead(), control.isMemWrite());

		//Writeback
		write_data = mux(alu_out, data_out, control.isMemtoReg());
		register.write(control.isRegWrite(), write_data);


		new_pc += 4;
		branch_pc = new_pc + (0<< 2);
		new_pc = mux(new_pc, branch_pc, control.isBranch() && alu_zero);
		pc.set(new_pc);
	}

	private int mux(int value1, int value2, boolean getSecond) {
		if(getSecond) {
			return value2;
		}
		return value1;
	}

	
	public boolean isDone() {
		System.out.println("instruction length:" + instructions.length() + "pc" + pc.get());
		return pc.get() >= instructions.length() || instructions.fetch(pc).isExit();
	}


	public int getPcValue() {
		return pc.get();
	}

	public int[] getRegisters() {
		return register.getRawData();
	}

	public int[] getMemory() {
		return memory.getRawData();
	}

	public List<Integer> getChangedRegisters() {
		return register.getChangedIndices();
	}

	public List<Integer> getChangedMemory() {
		return memory.getChangedIndices();
	}


}
