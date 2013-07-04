package pl.edu.agh.ki.mmorts.server.modules.basic.map.commons;

public enum RequestType {
	CHECK("check");
	
	private String messg;

	private RequestType(String messg) {
		this.messg = messg;
	}

	public String getMessg() {
		return messg;
	}
	
	
}
