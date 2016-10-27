package guru.springframework.swing.dto;

public class FaceMatchResultDTO {
	byte[] afid;
	float score;
	
	public FaceMatchResultDTO(byte[] afid, float score) {
		super();
		this.afid = afid;
		this.score = score;
	}
	public byte[] getAfid() {
		return afid;
	}
	public void setAfid(byte[] afid) {
		this.afid = afid;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
}
