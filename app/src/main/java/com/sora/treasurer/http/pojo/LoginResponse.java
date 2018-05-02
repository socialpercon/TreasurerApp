package com.sora.treasurer.http.pojo;

/**
 * Created by rupertdurano on 30/01/2018.
 */

public class LoginResponse extends GatewayResponse {

    private LoginData data;
    private LoginSb sb;

    public LoginData getData() { return data; }
    public void setData(LoginData data) { this.data = data; }

    public LoginSb getSb() { return sb; }
    public void setSb(LoginSb sb) { this.sb = sb; }

    public class LoginData {
        private String Session;
        private String[] Roles;
        private int[] Groups;
        private String FirstName;
        private String LastName;
        private String Email;
        private String Expires;
        private int MemberID;
        private String Five9StationID;
        private String[] Settings;
        private long ParticipantID;

        public String getSession() { return Session; }
        public void setSession(String session) { Session = session; }
        public String[] getRoles() { return Roles; }
        public void setRoles(String[] roles) { Roles = roles; }
        public int[] getGroups() { return Groups; }
        public void setGroups(int[] groups) { Groups = groups; }
        public String getFirstName() { return FirstName; }
        public void setFirstName(String firstName) { FirstName = firstName; }
        public String getLastName() { return LastName; }
        public void setLastName(String lastName) { LastName = lastName; }
        public String getEmail() { return Email; }
        public void setEmail(String email) { Email = email; }
        public String getExpires() { return Expires; }
        public void setExpires(String expires) { Expires = expires; }
        public int getMemberID() { return MemberID; }
        public void setMemberID(int memberID) { MemberID = memberID; }
        public String getFive9StationID() { return Five9StationID; }
        public void setFive9StationID(String five9StationID) { Five9StationID = five9StationID; }
        public String[] getSettings() { return Settings; }
        public void setSettings(String[] settings) { Settings = settings; }
        public long getParticipantID() { return ParticipantID; }
        public void setParticipantID(long participantID) { ParticipantID = participantID; }
    }

    public class LoginSb {
        private boolean Valid;
        private int HTTPCode;
        private boolean SecurityViolation;
        private boolean Required;
        private String[] Errors;

        public boolean isValid() { return Valid; }
        public void setValid(boolean valid) { Valid = valid; }
        public int getHTTPCode() { return HTTPCode; }
        public void setHTTPCode(int HTTPCode) { this.HTTPCode = HTTPCode; }
        public boolean isSecurityViolation() { return SecurityViolation; }
        public void setSecurityViolation(boolean securityViolation) { SecurityViolation = securityViolation; }
        public boolean isRequired() { return Required; }
        public void setRequired(boolean required) { Required = required; }
        public String[] getErrors() { return Errors; }
        public void setErrors(String[] errors) { Errors = errors; }
    }


}
