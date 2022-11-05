package com.lby.home.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private String title;
    private String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEditor.PostEditorBuilder builder() {
        return new PostEditor.PostEditorBuilder();
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditor.PostEditorBuilder title(String title) {
            if (title != null) {
                this.title = title;
            }
            return this;
        }

        public PostEditor.PostEditorBuilder content(String content) {
            if (content != null) {
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
