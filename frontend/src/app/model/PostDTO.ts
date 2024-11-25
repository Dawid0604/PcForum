import { UserProfileDTO } from "./UserProfileDTO"

export interface PostDTO {
    encryptedId: string,
    user: UserProfileDTO,
    createdAt: string,
    content: string,
    lastUpdatedAt: string
}