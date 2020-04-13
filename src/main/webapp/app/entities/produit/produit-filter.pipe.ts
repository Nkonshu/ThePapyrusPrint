import { Pipe, PipeTransform } from '@angular/core';
import { IProduit } from '../../shared/model/produit.model';

@Pipe({
  name: 'produitFilterPipe'
})
export class ProduitFilterPipe implements PipeTransform {
  transform(items: Array<IProduit> | undefined, nameSearch: string): any {
    if (!items) return [];
    if (!nameSearch) return items;

    return items.filter(item => {
      return Object.keys(item).some(key => {
        return String(item[key])
          .toLowerCase()
          .includes(nameSearch.toLowerCase());
      });
    });
  }
}
