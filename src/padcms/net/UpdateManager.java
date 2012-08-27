package padcms.net;


public class UpdateManager {
	public static final int DATABASE_EXIST = 1;
	public static final int DATABASE_NOT_EXIST = 2;
	public static final int DATABASE_SAVED = 3;
	public static final int START_DOWNLOAD_DB = 4;
	public static final int START_INIT_APPLICATION = 5;
	public static final int SHOW_ERROR = 6;
	public static final int SHOW_PROGRESS_DIALOG = 7;
	public static final int DISMISS_PROGRESS_DIALOG = 8;
	public static final int APPLICATION_DATA_UPDATE = 9;
	public static final int SQLDB_EXIST = 10;
	public static final int START_APPLICATION = 11;
	public static final int CHECK_APPLICATION_UPDATES = 12;
	public static final int GADGET_DATA_UPDATE = 13;
	public static final int START_DOWNLOAD_RESOURCES= 14;
	public static final int IS_AVALABLE_INTERNET= 15;
	public static final int NO_AVALABLE_INTERNET= 16;
	public static final int ITEM_DATA_UPDATE = 17;

//	public static void updateDataBase(int appID, final Handler callbackHandler) {
//
//		Client.sendRequest(RequestBuilder.buildReqest_GetSQLiteDatabase(appID),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent());
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//
//								Log.i("JSONObject reqest", "resalt" + json);
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//
//									if (jsonobj.has("d")) {
//										msg.what = START_DOWNLOAD_DB;
//										msg.obj = jsonobj.getString("d");
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//
//	public static void getDatabaseUrlByPass(String pass,
//			final Handler callbackHandler) {
//
//		Client.sendRequest(
//				RequestBuilder.buildReqest_GetSQLiteDatabaseByPassword(pass),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = cleanJsonObject(BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent()));
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//
//								Log.i("JSONObject reqest", "resalt" + json);
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//									if (jsonobj.has("d")) {
//										JSONObject oD = jsonobj
//												.getJSONObject("d");
//										if (oD.has("URL")) {
//											msg.what = START_DOWNLOAD_DB;
//											msg.obj = oD.get("URL");
//										} else if (oD.has("Error")) {
//											msg.what = SHOW_ERROR;
//											msg.obj = oD.get("Message");
//										}
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = json;
//									if (callbackHandler != null)
//										callbackHandler.sendMessage(msg);
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//
//	public static void uploadDatabase(final String urlToDB,
//			final Handler callbackHandler) {
//		URL url = null;
//
//		try {
//			url = new URL(urlToDB);
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		String fileName = url.getFile();
//		fileName = fileName.substring(fileName.lastIndexOf("/"));
//		final File fileDB = Constants.getPathDBFolder(fileName);
//		if (!fileDB.exists()) {
//			Client.sendRequest(RequestBuilder.buildReqest_LoadDB(urlToDB),
//					new ResponseListener() {
//						@Override
//						public void onResponseReceived(
//								final HttpResponse response) {
//							new Thread(new Runnable() {
//								@Override
//								public void run() {
//									Message msg = new Message();
//									InputStream in = null;
//
//									try {
//										in = response.getEntity().getContent();
//									} catch (Exception e) {
//										e.printStackTrace();
//										msg.what = SHOW_ERROR;
//										msg.obj = "Databse save Error";
//										if (callbackHandler != null) {
//											callbackHandler.sendMessage(msg);
//										}
//									}
//
//									if (in != null) {
//
//										if (BaseApplicationManager
//												.saveInputStreamToFile(fileDB
//														.getAbsolutePath(), in)) {
//											msg.what = START_INIT_APPLICATION;
//											msg.obj = fileDB.getAbsoluteFile();
//											if (callbackHandler != null) {
//												callbackHandler
//														.sendMessage(msg);
//											}
//										} else {
//											msg.what = SHOW_ERROR;
//											msg.obj = "Databse save Error";
//											if (callbackHandler != null) {
//												callbackHandler
//														.sendMessage(msg);
//											}
//										}
//									}
//
//								}
//							}).start();
//						}
//					});
//		} else {
//			Message msg = new Message();
//			msg.what = SQLDB_EXIST;
//			msg.obj = fileDB.getAbsoluteFile();
//			callbackHandler.sendMessage(msg);
//		}
//	}
//
//	public static void updateGadgetsInformaion(final int appID,
//			final Handler callbackHandler) {
//		Message msgDialog = new Message();
//		msgDialog.what = SHOW_PROGRESS_DIALOG;
//		callbackHandler.sendMessage(msgDialog);
//		Client.sendRequest(RequestBuilder.buildReqest_checkABTab(appID,
//				BaseApplicationManager.currentApp.tabsListCurrent),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent());
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//
//								Log.i("JSONObject reqest", "resalt" + json);
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//									if (jsonobj.has("d")) {
//										msg.what = GADGET_DATA_UPDATE;
//										msg.obj = jsonobj;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = json;
//									if (callbackHandler != null)
//										callbackHandler.sendMessage(msg);
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//
//	public static void updateApplicationInformaion(final Handler callbackHandler) {
//		Client.sendRequest(
//				RequestBuilder
//						.buildReqest_checkApplicationUpdates(BaseApplicationManager.currentApp),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = cleanJsonObject(BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent()));
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//								Log.i("JSONObject reqest", "resalt" + json);
//							
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//									if (jsonobj.has("d")) {
//										if(jsonobj.get("d").toString().length()>0)
//										{
//											msg.what = APPLICATION_DATA_UPDATE;
//											msg.obj = jsonobj.getJSONObject("d");
//										}else
//										{
//											msg.what = START_DOWNLOAD_RESOURCES;
//										}
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = json;
//									if (callbackHandler != null)
//										callbackHandler.sendMessage(msg);
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//	public static void updateApplicationTables(final Handler callbackHandler) {
//		Client.sendRequest(
//				RequestBuilder
//						.buildReqest_checkApplicationTablse(BaseApplicationManager.currentApp),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = cleanJsonObject(BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent()));
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//								json=json.replace("=\"","='").replace(";\"",";'").replace("\r\n","");
//								
//								Log.i("JSONObject reqest", "resalt" + json);
//								
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//									if (jsonobj.has("d")) {
//										if(jsonobj.get("d").toString().length()>0)
//										{
//											msg.what = APPLICATION_DATA_UPDATE;
//											msg.obj = jsonobj.getJSONObject("d");
//										}else
//										{
//											msg.what = START_DOWNLOAD_RESOURCES;
//										}
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = json;
//									if (callbackHandler != null)
//										callbackHandler.sendMessage(msg);
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//	
//
//
//	public static void updateApplicationInformaionV2(final Handler callbackHandler) {
//		Client.sendRequest(
//				RequestBuilder
//						.buildReqest_checkApplicationUpdates(BaseApplicationManager.currentApp),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = cleanJsonObject(BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent()));
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//								json=json.replace("=\"","='").replace(";\"",";'").replace("\r\n","");
//								Log.i("JSONObject reqest", "resalt" + json);
//							
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//									if (jsonobj.has("d")) {
//										if(jsonobj.get("d").toString().length()>0)
//										{
//											msg.what = APPLICATION_DATA_UPDATE;
//											msg.obj = jsonobj.getJSONObject("d");
//										}else
//										{
//											msg.what = START_DOWNLOAD_RESOURCES;
//										}
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = json;
//									if (callbackHandler != null)
//										callbackHandler.sendMessage(msg);
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//
//	public static void updateItemsByAppID(final Handler callbackHandler) {
//		Client.sendRequest(
//				RequestBuilder
//						.buildReqest_CheckABItemsByAppID(BaseApplicationManager.currentApp.getID(),BaseApplicationManager.currentApp.itemsList),
//				new ResponseListener() {
//					@Override
//					public void onResponseReceived(final HttpResponse response) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								Message msg = new Message();
//								String json = null;
//								try {
//									json = cleanJsonObject(BaseApplicationManager
//											.streamToString(response
//													.getEntity().getContent()));
//								} catch (Exception e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = "Server Error pleas try again.";
//									if (callbackHandler != null) {
//										callbackHandler.sendMessage(msg);
//									}
//								}
//								Log.i("JSONObject reqest", "resalt" + json);
//							
//								JSONObject jsonobj = null;
//								try {
//									jsonobj = new JSONObject(json);
//									if (jsonobj.has("d")) {
//										if(jsonobj.get("d").toString().length()>0)
//										{
//											msg.what = ITEM_DATA_UPDATE;
//											msg.obj = jsonobj.getJSONObject("d");
//										}else
//										{
//											msg.what = START_DOWNLOAD_RESOURCES;
//										}
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									} else {
//										msg.what = SHOW_ERROR;
//										if (jsonobj.has("Message"))
//											msg.obj = jsonobj
//													.getString("Message");
//										else
//											msg.obj = json;
//										if (callbackHandler != null)
//											callbackHandler.sendMessage(msg);
//									}
//								} catch (JSONException e) {
//									e.printStackTrace();
//									msg.what = SHOW_ERROR;
//									msg.obj = json;
//									if (callbackHandler != null)
//										callbackHandler.sendMessage(msg);
//								}
//
//							}
//						}).start();
//					}
//				});
//
//	}
//	private static String cleanJsonObject(String json) {
//		try {
//			JSONObject object = new JSONObject(json);
//			if (object.has("d")) {
//				String jsonrezalt = object.getString("d");
//				if (jsonrezalt.length() > 0) {
//					json=json.replace("\\r\\n", "").replace("\\", "")
//					 .replace("{\"d\":\"", "{\"d\":");
//					json=json.replace(json.substring(json.length()-3),"}");
//				} 
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return json;
//	}

	public static void updateGadgetInformaion() {

	}

	public static void updateGadgetInformaionByType() {

	}
}
