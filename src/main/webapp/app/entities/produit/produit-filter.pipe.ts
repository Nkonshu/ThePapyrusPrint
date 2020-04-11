import { Pipe, PipeTransform } from '@angular/core';
import { IProduit } from '../../shared/model/produit.model';

@Pipe({
  name: 'produitFilterPipe'
})
export class ProduitFilterPipe implements PipeTransform {
  transform(items: Array<IProduit> | undefined, nameSearch: string): any {
    if (items && items.length) {
      return items.filter(item => {
        if (nameSearch && item.nom!.toLowerCase().includes(nameSearch.toLowerCase()) === false) {
          return false;
        }
        return true;
      });
    } else {
      return items;
    }
  }
}
