package Java;

public class User {
	private String Email;
	private String Password;
	private String PhoneNumber;
	private String Address;
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public User(String email, String password, String phoneNumber, String address) {
		super();
		Email = email;
		Password = password;
		PhoneNumber = phoneNumber;
		Address = address;
	}
	public User() {
	
	}
	
	
	
}
