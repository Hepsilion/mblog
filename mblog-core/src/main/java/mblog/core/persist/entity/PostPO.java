/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package mblog.core.persist.entity;

import mblog.base.lang.EnumPrivacy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author langhsu
 * 
 */
@Entity
@Table(name = "mto_posts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Indexed(index = "posts")
public class PostPO implements Serializable {
	private static final long serialVersionUID = 7144425803920583495L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@DocumentId
	private long id;

	@Field
	@NumericField
	@Column(name = "group_", length = 5)
	private int group;

	@Field
	@Column(name = "title", length = 64)
	private String title; // 标题
	
	@Field
	private String summary; // 摘要

	@Field
	private String tags; // 标签

	@Field
	@NumericField
	@Column(name = "author_id")
	private long authorId; // 作者
	
	private String editor; // 编辑器
	
	@Lob
	@Basic(fetch = FetchType.LAZY) 
	@Type(type="text")
	private String content; // 内容
	
    @Lob
	@Basic(fetch = FetchType.LAZY) 
	@Type(type="text")
	private String markdown; // markdown 内容
	
	@Column(name = "last_image_id")
	private long lastImageId;

	@Temporal(value = TemporalType.TIMESTAMP)
	private Date created;

	/**
	 * @see EnumPrivacy
	 */
	@Field
	@NumericField
	private int privacy;  // 私密

	private int images; // 图片统计
	private int featured; // 推荐状态
	private int favors; // 喜欢数
	private int comments; // 评论数
	private int views; // 阅读数

	private int status; // 文章状态

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private PostAttribute attribute;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getLastImageId() {
		return lastImageId;
	}

	public void setLastImageId(long lastImageId) {
		this.lastImageId = lastImageId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFeatured() {
		return featured;
	}

	public void setFeatured(int featured) {
		this.featured = featured;
	}

	public int getFavors() {
		return favors;
	}

	public void setFavors(int favors) {
		this.favors = favors;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getMarkdown() {
		return markdown;
	}

	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public int getImages() {
		return images;
	}

	public void setImages(int images) {
		this.images = images;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public PostAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(PostAttribute attribute) {
		this.attribute = attribute;
	}
}