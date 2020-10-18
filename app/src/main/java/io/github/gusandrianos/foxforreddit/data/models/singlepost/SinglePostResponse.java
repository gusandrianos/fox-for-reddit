package io.github.gusandrianos.foxforreddit.data.models.singlepost;

import io.github.gusandrianos.foxforreddit.data.models.singlepost.comments.Comments;
import io.github.gusandrianos.foxforreddit.data.models.singlepost.post.SinglePost;

public class SinglePostResponse {
    private SinglePost singlepost;
    private Comments comments;

    public SinglePostResponse(SinglePost singlepost, Comments comments) {
        this.singlepost = singlepost;
        this.comments = comments;
    }

    public SinglePost getSinglepost() {
        return singlepost;
    }

    public Comments getComments() {
        return comments;
    }
}
