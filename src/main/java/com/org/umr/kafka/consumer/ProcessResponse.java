package com.org.demo.kafka.consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileException;

public class ProcessResponse {
	
	public static void formatResponse(String message) {
		final Logger LOGGER = LogManager.getLogger(ProcessResponse.class);
		MFResponseRec mf_rec = new MFResponseRec();
		try {
			LOGGER.info(String.format("$$ -> Consumed Message for -> %s",message));
			JSONObject jsonobj = new JSONObject(message);
			JSONObject originalRequest = (JSONObject)jsonobj.get("originalRequest");
			JSONObject islRequest = (JSONObject)originalRequest.get("islRequest");
			JSONObject searchRequest= (JSONObject)islRequest.get("searchRequest");
			String lst_nm = searchRequest.getString("lst_nm");
			mf_rec.setLst_nm(rightpad(lst_nm,20));
			String first_nm = searchRequest.getString("first_nm");
			mf_rec.setFirst_nm(rightpad(first_nm,14));
			String dob = searchRequest.getString("dob");
			mf_rec.setDob(rightpad(dob,10));
			String policy_nbr = searchRequest.getString("policy_nbr");
			mf_rec.setPolicy_nbr(rightpad(policy_nbr,8));
			String subscriber_id = searchRequest.getString("subscriber_id");
			mf_rec.setSubscriber_id(rightpad(subscriber_id,30));
			String reqIdentifier = originalRequest.getString("reqIdentifier");
			mf_rec.setReqIdentifier(rightpad(reqIdentifier,30));
			LOGGER.info(String.format("$$ -> Consumed Message for -> %s",reqIdentifier));
			JSONObject islResponse = (JSONObject)jsonobj.get("islResponse");
			Object status = islResponse.get("status");
			if (status.toString().equals("OK")) {
				JSONObject body = (JSONObject)islResponse.get("body");
				if(body.has("contactInfo")) {
					JSONObject contactInfo = (JSONObject)body.get("contactInfo");
					if (contactInfo.has("electronicContacts")) {
						JSONArray electronicContacts = (JSONArray)contactInfo.get("electronicContacts");
						JSONObject primaryelectronicContacts = (JSONObject)electronicContacts.get(0);
						if (primaryelectronicContacts.has("electronicAddress")){
							String electronicAddress = primaryelectronicContacts.getString("electronicAddress");
							mf_rec.setElectronicAddress(rightpad(electronicAddress,60));
						}else {
							String electronicAddress = "";
							mf_rec.setElectronicAddress(rightpad(electronicAddress,60));
						};
						if (primaryelectronicContacts.has("undeliveredReasonTypeCode")){
							String undeliveredReasonTypeCode = "Y";
							mf_rec.setUndeliveredReasonTypeCode(rightpad(undeliveredReasonTypeCode,5));
						}else {
							String undeliveredReasonTypeCode = "";
							mf_rec.setUndeliveredReasonTypeCode(rightpad(undeliveredReasonTypeCode,5));
						}
					}else{
						String electronicAddress = "";
						mf_rec.setElectronicAddress(rightpad(electronicAddress,60));
						String undeliveredReasonTypeCode = "";
						mf_rec.setUndeliveredReasonTypeCode(rightpad(undeliveredReasonTypeCode,5));
					}
				}else{
					String electronicAddress = "";
					mf_rec.setElectronicAddress(rightpad(electronicAddress,60));
					String undeliveredReasonTypeCode = "";
					mf_rec.setUndeliveredReasonTypeCode(rightpad(undeliveredReasonTypeCode,5));
				};
				//Default to a 'N'
				String paperlessIndicator = "N";
				mf_rec.setpaperlessIndicator(rightpad(paperlessIndicator,5));
				boolean EM_isSelected = false;
				boolean EM_isDefaultValue = false;
				boolean PM_isSelected = false;
				boolean continue_parsing = false;
				JSONObject preferenceCategory = null;
				JSONObject consumerPreferenceType = null;
				JSONObject property = null;
				JSONObject listValue = null;
				if (body.has("preferences")){
					JSONObject preferences = (JSONObject)body.get("preferences");
					if(preferences.has("preferenceCategories")) {
						JSONArray preferenceCategories = (JSONArray)preferences.get("preferenceCategories");
						for(int i=0;i<preferenceCategories.length();i++ ) {
							preferenceCategory = preferenceCategories.getJSONObject(i);
							if(preferenceCategory.has("name")) {
								if(preferenceCategory.getString("name").equals("Paperless_Settings")) {
									continue_parsing = true;
									break;
								}
							}
						}
					}
				}
				if (continue_parsing) {
					continue_parsing = false;
					if(preferenceCategory.has("consumerPreferenceTypes")) {
						JSONArray consumerPreferenceTypes = (JSONArray) preferenceCategory.get("consumerPreferenceTypes");
						for(int i=0;i<consumerPreferenceTypes.length();i++ ) {
							consumerPreferenceType = consumerPreferenceTypes.getJSONObject(i);
							if(consumerPreferenceType.has("name")) {
								if(consumerPreferenceType.getString("name").equals("Required_documents")) {
									continue_parsing = true;
									break;
								}
							}
						} 
					}
				}
				if (continue_parsing) {
					continue_parsing = false;
					if(consumerPreferenceType.has("properties")) {
						JSONArray properties = (JSONArray) consumerPreferenceType.get("properties");
						for(int i=0;i<properties.length();i++ ) {
							property = properties.getJSONObject(i);
							if(property.has("name")) {
								if(property.getString("name").equals("Document_Delivery_Channel")) {
									continue_parsing = true;
									break;
								}
							}
						}
					}
				}
				if (continue_parsing) {
					if(property.has("listValues")) {
						JSONArray listValues = (JSONArray) property.get("listValues");
						for(int i=0;i<listValues.length();i++ ) {
							listValue = listValues.getJSONObject(i);
							if(listValue.has("code")) {
								if(listValue.getString("code").equals("EM")) {
									EM_isSelected = listValue.getBoolean("isSelected");
									EM_isDefaultValue = listValue.getBoolean("isDefaultValue");
								}
								if(listValue.getString("code").equals("PM")) {
									PM_isSelected = listValue.getBoolean("isSelected");
								}
							}
						}
					}
				}
				if ((EM_isSelected)||((!PM_isSelected)&&(EM_isDefaultValue))) {													
					paperlessIndicator = "Y";
					mf_rec.setpaperlessIndicator(rightpad(paperlessIndicator,5));
				}
				ZFile output = new ZFile("//DD:OUTPUT", "wb,type=record,recfm=fb,lrecl=200,noseek");
				output.write(mf_rec.toOutput().getBytes());
				output.close();
			}
			else {
				mf_rec.setStatus(rightpad(status.toString(),20));
				ZFile error = new ZFile("//DD:ERROR", "wb,type=record,recfm=fb,lrecl=200,noseek");
				error.write(mf_rec.toError().getBytes());
				error.close();
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			} 
		catch (ZFileException e) {
			e.printStackTrace();
		} 
	}
	private static String rightpad(String text, int length) {
	    return String.format("%-" + length + "." + length + "s", text);
	}
}