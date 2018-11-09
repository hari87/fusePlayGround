package nl.devoteam;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;*/

//@Getter @Setter @NoArgsConstructor - got to use lombok, however
//I am not able to getter setter arguments correctly. Need to check. 
//@NoArgsConstructor
//@RequiredArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
	private int id;
	@Column(name="user_name")
	private String user_name;
	@Column(name="client_name")
	private String client_name;
	    

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getClient_name() {
			return client_name;
		}

		public void setClient_name(String client_name) {
			this.client_name = client_name;
		}

		   
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public User(String user_name, String client_name, int id) {
		        this.user_name = user_name;
		        this.client_name = client_name;
		        this.id = id;
		    }
		public User() {
			
		}
	
}
