package mblog.core.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Created by langhsu on 2015/10/25.
 */
@Entity
@Table(name = "mto_posts_attribute")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostAttribute implements Serializable {
	private static final long serialVersionUID = 7829351358884064647L;

	@Id
    @GeneratedValue(generator = "pkGenerator")
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = @Parameter(name = "property", value = "post"))
    private long id;

    @OneToOne(mappedBy = "attribute")
    @JoinColumn(name = "id", referencedColumnName = "id")
    private PostPO post;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "video_body")
    private String videoBody;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PostPO getPost() {
        return post;
    }

    public void setPost(PostPO post) {
        this.post = post;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoBody() {
        return videoBody;
    }

    public void setVideoBody(String videoBody) {
        this.videoBody = videoBody;
    }
}
