package Java;

public class Order extends Product{

	
	private String orderName;
	private String uid;
	private int quantity;
	private String date;
	public Order() {
	}
	public Order(String orderName, String uid, int quantity,String date) {
		super();
		this.orderName = orderName;
		this.uid = uid;
		this.quantity = quantity;
		this.date = date;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String string) {
		this.uid = string;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Order [orderName=" + orderName + ", uid=" + uid + ", quantity=" + quantity + ", date=" + date + "]";
	}
	
	
	
	
}
