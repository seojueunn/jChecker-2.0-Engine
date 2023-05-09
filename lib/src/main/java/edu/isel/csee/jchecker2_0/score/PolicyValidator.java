package edu.isel.csee.jchecker.score;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PolicyValidator {
    private ArrayList<String> inputs = null;
    private ArrayList<String> checksums = null;
    private boolean hasChecksum = false;

    public PolicyValidator() {

    }

    // if we use checksum data, this method returns true
    public void setHasChecksum(ArrayList<String> checksumArray) {
        // when we set the grading policy, jChecker front-end fills the checksum part with one empty string
        // if we do not use checksum data in the homework, the number of checksum data is one and data is an empty string
        if ((checksumArray.size() == 1) && checksumArray.get(0).isEmpty()) {
            this.hasChecksum = false;
        }
        this.hasChecksum = true;
    }

    // get hasChecksum data (if we use checksum data -> hasChecksum is true)
    public boolean getHasChecksum() {
        return this.hasChecksum;
    }

    public void validator(JsonObject policy) {
        // validation for classes deduct point
        JsonObject obj = new Gson().fromJson(policy.get("classes"), JsonObject.class);
        if (!obj.has("deductPoint")) {
            obj.addProperty("deductPoint", 0.0);
            policy.add("classes", obj);
        }
        if (!obj.has("maxDeduct")) {
            obj.addProperty("maxDeduct", 0.0);
            policy.add("classes", obj);
        }

        // validation for checksum data (oracle state is true)
        obj = new Gson().fromJson(policy.get("oracle"), JsonObject.class);
        if (!obj.get("state").toString().equals("false")) {
            inputs = new Gson().fromJson(obj.get("input"), new TypeToken<ArrayList<String>>() {
            }.getType());
            checksums = new Gson().fromJson(obj.get("checksum"), new TypeToken<ArrayList<String>>() {
            }.getType());

            setHasChecksum(checksums);
            // if we use checksum, the number of checksum data is equal to input data size
            if (getHasChecksum()) {
                for (int i = 0; i < checksums.size(); i ++) {
                    // replace null data to empty string
                    if (checksums.get(i) == null) {
                        checksums.set(i, "");
                    }
                }

                // #8 - input size > checksum size -> add some empty string in checksum data
                if (inputs.size() > checksums.size()) {
                    while (inputs.size() != checksums.size()) {
                        checksums.add("");
                    }
                }

                policy.getAsJsonObject("oracle").remove("checksum"); // remove original checksum data
                policy.getAsJsonObject("oracle").add("checksum", (new Gson()).toJsonTree(checksums)); // add new
            }
        }
    }
}
