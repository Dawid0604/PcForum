import { UserProfileDTO } from "./UserProfileDTO"

export interface ThreadDetailsDTO {
    encryptedId: string,
    title: string,
    content: string,
    createdAt: string,
    lastUpdatedAt: string,
    lastActivity: string,
    isClosed: boolean,
    user: UserProfileDTO,
    categoryName: string,
    subCategoryName: string,
    loggedUserThread: boolean
}