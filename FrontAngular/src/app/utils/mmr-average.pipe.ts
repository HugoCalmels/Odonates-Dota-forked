import { Pipe, PipeTransform } from "@angular/core";
import { User } from "../models/user.model";

@Pipe({
  name: 'mmrAverage'
})
export class MmrAveragePipe implements PipeTransform {

  private readonly mmrMapping: { [key: string]: number } = {
    '10': 0,
    '11': 154,
    '12': 308,
    '13': 462,
    '14': 616,
    '20': 770,
    '21': 924,
    '22': 1078,
    '23': 1232,
    '24': 1386,
    '30': 1540,
    '31': 1694,
    '32': 1848,
    '33': 2002,
    '34': 2156,
    '40': 2310,
    '41': 2464,
    '42': 2618,
    '43': 2772,
    '44': 2926,
    '50': 3080,
    '51': 3234,
    '52': 3388,
    '53': 3542,
    '54': 3696,
    '60': 3850,
    '61': 4004,
    '62': 4158,
    '63': 4312,
    '64': 4466,
    '70': 4620,
    '71': 4820,
    '72': 5020,
    '73': 5220,
    '74': 5420,
    '80': 6000, 
    '81': 7000, 
    '82': 8000, 
    '83': 9000, 
    '84': 10000, 
  };

  transform(users: User[]): number {
    if (!users || !Array.isArray(users) || users.length === 0) {
        return 0; 
    }

    const validUsers = users.filter(user => user && user.rankTier && this.mmrMapping.hasOwnProperty(user.rankTier));
    if (validUsers.length === 0) {
        return 0; 
    }

    const totalMmr = validUsers.reduce((sum, user) => {
        const mmr = this.mmrMapping[user.rankTier];
        return sum + mmr;
    }, 0);

    const averageMmr = totalMmr / validUsers.length;

    const roundedAverageMmr = Math.round(averageMmr / 50) * 50;

    return roundedAverageMmr;
  }
}