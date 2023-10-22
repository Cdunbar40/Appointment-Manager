package dunbar.c195pa.Model;

/**Class used to store the information needed for the Appointments By Type report.*/
public class ReportBlock {

    /**The Month for the type count.*/
    private String month;
    /**The type of appointment counted*/
    private String type;
    /**The total number of appointments of the given type in the given month*/
    private int count;

    /**Report block constructor
     * @param type The type of appointment counted
     * @param count The total number of appointments of the given type in the given month
     * @param month The month for the type count*/
    public ReportBlock(String month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    /**Getter method for the type.
     * @return Returns the type of appointment being counted*/
    public String getType() {
        return type;
    }

    /**Getter method for the month
     * @return returns the month for the type/count combination*/
    public String getMonth() {
        return month;
    }

    /**Getter method for the count
     * @return returns the count of appointment types by month*/
    public int getCount() {
        return count;
    }

}
