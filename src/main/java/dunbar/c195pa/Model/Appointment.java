package dunbar.c195pa.Model;

import dunbar.c195pa.DAO.ContactQuery;

import java.time.LocalDateTime;

/**Contains all data members necessary to retrieve and store appointments in the database*/
public class Appointment {
    /**Unique appointment ID*/
    private int appointmentID;
    /**Appointment Title*/
    private String title;
    /**Appointment description*/
    private String description;
    /**Appointment location*/
    private String location;
    /**ID number of the contact associated with the appointment*/
    private int contactID;
    /**The type of appointment*/
    private String type;
    /**The start datetime of the appointment*/
    private LocalDateTime start;
    /**The end datetime of the appointment*/
    private LocalDateTime end;
    /**The ID of the user associated with the appointment*/
    private int userID;
    /**The Id of the customer associated with the appointment*/
    private int customerID;
    /**The name of the contact associated with the appointment - used for display throughout the application */
    private String contactName;

    /**Appointment constructor. In addition to populating directly input fields, determines the contact name based
     *  off of the contact ID.
     *  @param userID ID of the user associated with the appointment
     *  @param appointmentID Unique appointment ID
     *  @param customerID ID of the customer associated with the appointment
     *  @param type Type of appointment
     *  @param title Title of appointment
     *  @param start The start datetime of the appointment
     *  @param location the location of the appointment
     *  @param end The end datetime of the appointment
     *  @param description A description of the appointment
     *  @param contactID The ID of the contact associated with the appointment*/
    public Appointment(int appointmentID, String title, String description, String location, int contactID, String type, LocalDateTime start, LocalDateTime end, int userID, int customerID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.type = type;
        this.start = start;
        this.end = end;
        this.userID = userID;
        this.customerID = customerID;
        for(Contact contact: ContactQuery.getAllContacts()){
            if(contact.getContactID() == contactID){
                contactName = contact.getName();
                break;
            }
        }
    }

    /**Getter method for the Appointment ID.
     * @return Returns the appointment ID.*/
    public int getAppointmentID() {
        return appointmentID;
    }

    /**Getter method for the appointment title/
     * @return Returns the appointment title*/
    public String getTitle() {
        return title;
    }

    /**Setter method for the title.
     * @param title The appointment title*/
    public void setTitle(String title) {
        this.title = title;
    }
    /**Getter method for the description.
     * @return Returns the appointment description*/
    public String getDescription() {
        return description;
    }

    /**Setter method for the description
     * @param description The appointment description*/
    public void setDescription(String description) {
        this.description = description;
    }

    /**Getter method for the location
     * @return Returns the appointment location*/
    public String getLocation() {
        return location;
    }

    /**Setter method for the location
     * @param location The location of the appointment*/
    public void setLocation(String location) {
        this.location = location;
    }

    /**Getter method for the type.
     * @return Returns the type of appointment*/
    public String getType() {
        return type;
    }

    /**Setter method for the type.
     * @param type The type of appointment*/
    public void setType(String type) {
        this.type = type;
    }

    /**Getter method for the start datetime.
     * @return Returns the start date/time of the appointment. */
    public LocalDateTime getStart() {
        return start;
    }

    /**Setter method for the start datetime.
     * @param start The start datetime of the appointment*/
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**Getter method for the end datetime.
     * @return Returns the end datetime of the appointment*/
    public LocalDateTime getEnd() {
        return end;
    }

    /**Setter method for the end datetime.
     * @param end The end datetime of the appointment*/
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**Getter method for the user ID.
     * @return Returns the user ID associated with the appointment.*/
    public int getUserID() {
        return userID;
    }

    /**Getter method for the customer ID.
     * @return Returns the customer ID associated with the appointment.*/
    public int getCustomerID() {
        return customerID;
    }

    /**Setter method for the customer ID.
     * @param customerID The customer ID associated with the appointment*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**Getter method for the Contact Name.
     * @return  Returns the name of the Contact associated with the appointment.*/
    public String getContactName(){
        return contactName;
    }
}
