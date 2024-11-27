import { UserProfileDTO } from "./UserProfileDTO"

export interface NewestPostDTO {
    encryptedId: string,
    user: UserProfileDTO,
    createdAt: string,
    thread: Thread
}

interface Thread {
    encryptedId: string,
    title: string
}