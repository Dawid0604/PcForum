export interface PostContentDTO {
    content: string[],    
    meta: PostBlockquoteMeta
}

export interface PostBlockquoteMeta {
    authorNickname: string,
    dateAdded: string
}