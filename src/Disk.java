
public class Disk {

	boolean busy;
	String operation;
	String data;
	int size;

	public Disk(int size) {
		busy = false;
		operation = "";
		this.size = size;
		for (int i = 0; i < size; i++) {
			data += (char) (Math.random() * 256);
		}
	}

	public String readFrom(int address, int length) {
		busy = true;
		operation = "Read";
		String s = data.substring(address, address + length);
		busy = false;
		operation = "";
		return s;
	}

	public void writeTo(int address, String data) {
		busy = true;
		operation = "Write";
		this.data = this.data.substring(0, address) + data;
		busy = false;
		operation = "";
	}

}
