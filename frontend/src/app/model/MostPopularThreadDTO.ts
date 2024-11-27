import { UserProfileDTO } from "./UserProfileDTO";

export interface MostPopularThreadDTO {
    encryptedId: string,
    user: UserProfileDTO,
    createdAt: string,
    title: string,
    numberOfPosts: number
}