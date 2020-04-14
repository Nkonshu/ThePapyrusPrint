import { Pipe, PipeTransform } from '@angular/core';
import { IProduit } from '../../shared/model/produit.model';

@Pipe({
  name: 'produitFilterPipe'
})
export class ProduitFilterPipe implements PipeTransform {
  transform(items: Array<IProduit> | undefined, nameSearch: string): any {
    if (!items) return [];
    if (!nameSearch) return items;

    const parts = nameSearch && nameSearch.trim().split(/\s+/),
      keys = Object.keys(items[0]);

    if (!parts || !parts.length) return items;

    return items.filter(function(obj): any {
      return parts.every(function(part): any {
        return keys.some(function(key): any {
          return String(obj[key])
            .toLowerCase()
            .includes(part.toLowerCase());
        });
      });
    });
  }
  // return items.filter(item => {
  //    return Object.keys(item).some(key => {
  //      return String(item[key])
  //        .toLowerCase()
  //        .includes(nameSearch.toLowerCase());
  //    });
  // });
}
