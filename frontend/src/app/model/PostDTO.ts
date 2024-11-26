import { PostEntityContentDTO } from "./PostEntityContentDTO"
import { UserProfileDTO } from "./UserProfileDTO"

export interface PostDTO {
    encryptedId: string,
    user: UserProfileDTO,
    createdAt: string,
    content: PostEntityContentDTO[],
    lastUpdatedAt: string
}