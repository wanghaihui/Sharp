package com.conquer.sharp.agora;

public class Token {

    /**
     * result : {"token":"00652c9afa2ac4f40ab8132448a170f6f5eIABPH0Sziq9zAMGix2f7n9YhYteZUk/md0loYuIRRRgH1QI3bl4RMGWjCgDoVgEAYH/WXAAA"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * token : 00652c9afa2ac4f40ab8132448a170f6f5eIABPH0Sziq9zAMGix2f7n9YhYteZUk/md0loYuIRRRgH1QI3bl4RMGWjCgDoVgEAYH/WXAAA
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
