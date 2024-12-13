import { PostContentDTO } from "./PostContentDTO"
import { UserProfileDTO } from "./UserProfileDTO"

export interface ExtendedPostDTO {
    encryptedId: string,
    user: UserProfileDTO,
    createdAt: string,
    thread: Thread,
    content: PostContentDTO[]
}

export interface Thread {
    encryptedId: string,
    title: string
}