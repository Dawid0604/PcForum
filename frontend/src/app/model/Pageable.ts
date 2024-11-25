export interface Pageable<T> {
    content: T[],
    number: number,
    pageSize: number,
    last: number,
    first: number,
    totalElements: number,
    totalPages: number,
    size: number,
    empty: boolean
}