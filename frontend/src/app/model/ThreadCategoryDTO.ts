import { ThreadDTO } from "./ThreadDTO";

export interface ThreadCategoryDTO {
    encryptedId: string,
    name: string,
    description: string,
    subCategories: ThreadCategoryDTO[],
    numberOfThreads: number,
    numberOfPosts: number,
    newestThread: ThreadDTO,
    thumbnailPath: string
}