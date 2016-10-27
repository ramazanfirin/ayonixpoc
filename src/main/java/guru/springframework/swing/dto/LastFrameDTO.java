package guru.springframework.swing.dto;

public class LastFrameDTO {
	String name;
	long currentMiliseconds;
	
	
	public LastFrameDTO(String name, long currentMiliseconds) {
		super();
		this.name = name;
		this.currentMiliseconds = currentMiliseconds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCurrentMiliseconds() {
		return currentMiliseconds;
	}
	public void setCurrentMiliseconds(long currentMiliseconds) {
		this.currentMiliseconds = currentMiliseconds;
	}
}
