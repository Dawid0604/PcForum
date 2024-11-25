import { ThreadDTO } from "./ThreadDTO";
import { ThreadCategoryDTO } from "./ThreadCategoryDTO";

export interface GroupedThreadCategoryDTO {
    encryptedId: string,
    name: string,
    numberOfRows: number,
    newestThread: ThreadDTO,
    subCategories: ThreadCategoryDTO[]
}