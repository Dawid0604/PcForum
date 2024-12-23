import { ThreadDTO } from "./ThreadDTO"

export interface UserProfileThreadsDTO {
    nickname: string,
    avatar: string,
    encryptedId: string,
    threads: ThreadDTO[]
}