package com.hcu.hot6.domain.response;

import com.hcu.hot6.domain.Position;
import com.hcu.hot6.domain.Post;
import com.hcu.hot6.domain.Poster;
import com.hcu.hot6.domain.Thumbnail;
import com.hcu.hot6.domain.request.PositionForm;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.hcu.hot6.util.Utils.toArray;
import static com.hcu.hot6.util.Utils.toDate;

@Getter
@NoArgsConstructor
public class PostReadOneResponse {
    private Long id;

    // Thumbnail
    private String title;
    private String summary;
    private Date recruitStart;
    private Date recruitEnd;
    private Date projectStart;
    private List<String> durations;
    private List<String> tags;
    private boolean isClosed;
    private boolean isArchived;

    // Post
    private String author;
    private List<String> postTypes;
    private String contact;

    // Post optional
    private String content;
    private String contactDetails;
    private List<String> targetYears;
    private List<String> targetDepartment;
    private List<String> keywords;
    private List<PositionForm> positions;
    private List<String> postURL;


    private int nBookmark;
    private Long views;
    private Date createdDate;
    private Date lastModifiedDate;
    private boolean isVerified;
    private boolean hasLiked;

    public PostReadOneResponse(Post post, String email) {
        final Thumbnail thumbnail = post.getThumbnail();

        this.id = post.getId();
        this.title = thumbnail.getTitle();
        this.summary = thumbnail.getSummary();
        this.recruitStart = toDate(thumbnail.getRecruitStart());
        this.recruitEnd = toDate(thumbnail.getRecruitEnd());
        this.projectStart = toDate(thumbnail.getProjectStart());
        this.durations = toArray(thumbnail.getDurations(), ",");
        this.tags = toArray(thumbnail.getTags(), ";"); // 프론트에서 각 라인별로 ,로 구분 후 나타내기
        this.isClosed = thumbnail.isClosed();
        this.isArchived = thumbnail.isArchived();

        this.author = post.getAuthor().getNickname();
        this.postTypes = toArray(post.getPostTypes(), ",");
        this.contact = post.getContact();
        this.content = post.getContent();
        this.contactDetails = post.getContactDetails();
        this.targetYears = toArray(post.getTargetYears(), ",");
        this.targetDepartment = toArray(post.getTargetDepartment(), ",");
        this.keywords = toArray(post.getKeywords(), ",");
        this.positions = post.getPositions().stream()
                .map(Position::toResponse)
                .toList();
        this.postURL = post.getPosters().stream()
                .map(Poster::getPostURL)
                .toList();

        this.nBookmark = post.getBookmarks().size();
        this.views = post.getViews();
        this.createdDate = toDate(post.getCreatedDate());
        this.lastModifiedDate = toDate(post.getLastModifiedDate());
        this.isVerified = email.equals(post.getAuthor().getEmail());
        this.hasLiked = post.getBookmarks().stream()
                .anyMatch(bookmark -> email.equals(bookmark.getMember().getEmail()));
    }
}
