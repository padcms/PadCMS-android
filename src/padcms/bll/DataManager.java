package padcms.bll;



public class DataManager {
	//private static ArrayList<Issue> issue_list;
	
	public static void loadCachedData()
	{
//		SQLiteDatabase sqlDB=SQLiteManager.get_inctance().getReadableDatabase();
//		issue_list=getIssuesFromDB(sqlDB);
//		for(Issue issue:issue_list)
//		{
//			issue.setRevision_list(getRevisionsFromDB(sqlDB, issue));
//		}
	}
//	public static ArrayList<Issue> getIssuesFromDB(SQLiteDatabase sqlDB) {
////		ArrayList<Issue> issues = new ArrayList<Issue>();
////		Cursor cursorIssue = SQLiteManager
////				.getTableCursor(sqlDB,SQLiteManager.TABLE_NAME_Issue);
////		if (cursorIssue != null) {
////			for (int i = 0; i < cursorIssue.getCount(); i++) {
////				Issue issue = new Issue(cursorIssue.getInt(cursorIssue
////						.getColumnIndex("issue_id")),
////						cursorIssue.getString(cursorIssue
////								.getColumnIndex("issue_title")),
////						cursorIssue.getString(cursorIssue
////								.getColumnIndex("issue_product_id")),
////						Boolean.valueOf(cursorIssue.getString(cursorIssue
////								.getColumnIndex("paid"))));
////
////				issues.add(issue);
////				cursorIssue.moveToNext();
////			}
////			cursorIssue.close();
////		}
////		return issues;
//		return null;
//	}
//	public static ArrayList<Revision> getRevisionsFromDB(SQLiteDatabase sqlDB,
//			Issue issue) {
////		ArrayList<Revision> revisions = new ArrayList<Revision>();
////		Cursor cursorRevision = SQLiteManager.getTableCursor(sqlDB,
////				SQLiteManager.TABLE_NAME_Revision, "issue_id=?",
////				new String[issue.getIssue_id()], "revision_id");
////		if (cursorRevision != null) {
////			for (int i = 0; i < cursorRevision.getCount(); i++) {
////				Revision revision = new Revision(
////						cursorRevision.getInt(cursorRevision
////								.getColumnIndex("revision_id")),
////						cursorRevision.getString(cursorRevision
////								.getColumnIndex("revision_title")),
////						cursorRevision.getString(cursorRevision
////								.getColumnIndex("revision_cover_image")),
////						cursorRevision.getString(cursorRevision
////								.getColumnIndex("revision_cover_image_thumbnail")),
////						cursorRevision.getString(cursorRevision
////								.getColumnIndex("revision_cover_image_list")),
////						cursorRevision.getInt(cursorRevision
////								.getColumnIndex("etalon_width")),
////						cursorRevision.getInt(cursorRevision
////								.getColumnIndex("etalon_height")),
////						cursorRevision.getString(cursorRevision
////								.getColumnIndex("revision_updated")),
////						cursorRevision.getString(cursorRevision
////								.getColumnIndex("revision_published")));
////
////				revisions.add(revision);
////				cursorRevision.moveToNext();
////			}
////			cursorRevision.close();
////			
////		}
////		return revisions;
//		return null;
//	}
}
