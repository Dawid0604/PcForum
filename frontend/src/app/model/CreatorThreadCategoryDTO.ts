export interface CreatorThreadCategoryDTO {
    name: string,
    subCategories: CreatorThreadSubCategoryDTO[]
}

export interface CreatorThreadSubCategoryDTO {
    encryptedId: string,
    name: string
}
