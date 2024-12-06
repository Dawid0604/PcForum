import { ThreadNewestPostDTO } from "./ThreadNewestPostDTO";


export interface ThreadDTO {
    encryptedId: string,
    title: string,
    authorNickname: string,
    authorEncryptedId: string,
    authorAvatar: string,
    lastActivityDate: string,
    categoryName: string,
    newestPost: ThreadNewestPostDTO,
    numberOfViews: number,
    numberOfPosts: number
}