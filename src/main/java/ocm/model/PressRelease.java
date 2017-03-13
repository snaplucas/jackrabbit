package ocm.model;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import java.util.Date;

@Node(jcrMixinTypes = JcrConstants.MIX_VERSIONABLE)
public class PressRelease {
    @Field(path = true)
    private String path;

    @Field
    private String title;

    @Field
    private Date pubDate;

    @Field
    private String content;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
