package Model;



/**
 * Represents a report containing information about a certain type of report, month, and total.
 */
public class MonthlyAppointmentsByType {
    private String type;
    private String month;
    private Integer total;
    /**
     * Constructs a MonthlyAppointmentsByType object with the specified report information.
     *
     * @param type The type of the report.
     * @param month The month of the report.
     * @param total The total associated with the report.
     */
    public MonthlyAppointmentsByType(String type, String month,  int total) {
        this.type = type;
        this.month = month;
        this.total = total;
    }
    /**
     * Gets the type of the report.
     *
     * @return The type of the report.
     */
    public String getType() {

        return type;
    }
    /**
     * Sets the type of the report.
     *
     * @param type The type of the report to set.
     */
    public void setType(String type) {

        this.type = type;
    }
    /**
     * Gets the month of the report.
     *
     * @return The month of the report.
     */
    public String getMonth() {
        return month;
    }
    /**
     * Sets the month of the report.
     *
     * @param month The month of the report to set.
     */
    public void setMonth(String month) {
        this.month = month;
    }
    /**
     * Gets the total associated with the report.
     *
     * @return The total associated with the report.
     */
    public int getTotal() {

        return total;
    }
    /**
     * Sets the total associated with the report.
     *
     * @param total The total associated with the report to set.
     */
    public void setTotal(int total) {

        this.total = total;
    }

}
