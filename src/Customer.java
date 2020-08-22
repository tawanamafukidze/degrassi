public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;

    public Customer(String firstName, String lastName, String email, String password, String phone) {
        if (FieldLengthInvalid(firstName, 1, 30)) {
            //First Name Not Provided.
            return;
        }

        if (FieldLengthInvalid(lastName, 1, 50)) {
            //Last Name Not Provided.
            return;
        }

        if (!EmailValid(email)) {
            //Email is Not Valid.
            return;
        }
        //TODO: if email is unique

        if (FieldLengthInvalid(password, 1, 120)) {
            //Password is Not Valid.
            return;
        }

        if (FieldLengthInvalid(phone, 10, 10)) {
            //Phone is valid.
            return;
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;

        //TODO: commit to customer database
    }

    public Customer(String email, String password) {
        //TODO: query customer table for email
        //TODO: if user supplied password matches the password in the database
    }

    //checks if provided field is within the input limit and returns true, otherwise false.
    private boolean FieldLengthInvalid(String field, int minLength, int maxLength) {
        int fieldLength = field.length();
        return fieldLength < minLength || fieldLength > maxLength;
    }

    private boolean EmailValid(String email) {
        if (FieldLengthInvalid(email, 3, 60)) return false;
        return email.matches("[\\w+.-]+@{1}\\w+.[\\w]+");
    }

    public boolean changePassword(String password) {
        //TODO: change password
        //TODO: return true if password has been changed
        return true;
    }

    public boolean changEmail() {
        //TODO: change password
        //TODO: return true if password has been changed
        return true;
    }

    public void emptyCart(){
        //TODO: Empty cart
    }

    public void updateCart(Item[] items) {
        for (Item item : items){
            //TODO: add to items table
        }
    }

    public void purchase(){
        //TODO: Empty cart
    }

    public static void main(String[] args) {
    }
}