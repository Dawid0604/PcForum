import { ExtendedPostDTO } from "./ExtendedPostDTO";

export interface UserProfilePostsDTO {
    nickname: string,
    avatar: string,
    encryptedId: string,
    posts: ExtendedPostDTO[]
}