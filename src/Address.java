/**
 *
 * @author andrew.horner
 * @version 2023.12.02
 * Basic address format with getter methods
 */
public class Address {
    
    private int postal_code;
    private String street;
    private String city;
    private String state;
    private int zip;
    private String country;
    
    public Address(int postal_code, String street, String city, String state, int zip, String country){
        this.postal_code = postal_code;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
    }
    
    public int getPostalCode(){return postal_code;}
    
    public String getStreet(){return street;}
    
    public String getCity(){return city;}
    
    public String getState(){return state;}
    
    public int getZip(){return zip;}
    
    public String getCountry(){return country;}
    
    public String toString(){return postal_code + " " + street + " " + city + ", " + state + " " + zip + " " + country;}
}
