import { ThreadDTO } from "./ThreadDTO";

export interface ThreadCategoryDTO {
    encryptedId: string,
    name: string,
    description: string,
    subCategories: ThreadCategoryDTO[],
    numberOfRows: number,
    newestThread: ThreadDTO
}