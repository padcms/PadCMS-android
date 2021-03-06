package padcms.dao.issue.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table NOTE.
 */
public class Menu {

	private Long id;
	private String title;
	private Long firstpage_id;
	private String description;
	private String thumb_stripe;
	private String thumb_summary;
	private String color;
	

	public Menu() {
	}

	public Menu(Long id) {
		this.id = id;
	}

	public Menu(Long id, String title, Long firstpage_id, String description,
			String thumb_stripe, String thumb_summary, String color) {
		super();
		this.id = id;
		this.title = title;
		this.firstpage_id = firstpage_id;
		this.description = description;
		this.thumb_stripe = thumb_stripe;
		this.thumb_summary = thumb_summary;
		this.color = color;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getFirstpage_id() {
		return firstpage_id;
	}

	public void setFirstpage_id(Long firstpage_id) {
		this.firstpage_id = firstpage_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumb_stripe() {
		return thumb_stripe;
	}

	public void setThumb_stripe(String thumb_stripe) {
		this.thumb_stripe = thumb_stripe;
	}

	public String getThumb_summary() {
		return thumb_summary;
	}

	public void setThumb_summary(String thumb_summary) {
		this.thumb_summary = thumb_summary;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


}
