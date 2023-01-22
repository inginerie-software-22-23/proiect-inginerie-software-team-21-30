import { IUser } from "./user.interface";

export interface IRecipe {
    id: number;
    name: string;
    description: string;
    ingredients?: string;
    estimatedPrepTimeInMinutes?: number;
    image?: string;
}

