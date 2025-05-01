import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Observable, of, switchMap, tap, catchError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

interface Variant {
  id: number;
  variantName: string;
  additionalPrice: number;
  quantity: number;
}

interface ProductAttribute {
  attributeName: string;
  attributeValue: string;
}

interface Review {
  id: number;
  rating: number;
  comment: string;
  createdAt: string;
  user: {
    firstName: string;
    lastName: string;
  };
}

interface Seller {
  id: number;
  storeName: string;
  logoUrl: string;
  rating: number;
  totalReviews: number;
}

interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  discountPrice?: number;
  quantity: number;
  rating: number;
  totalReviews: number;
  images: { imageUrl: string }[];
  attributes: ProductAttribute[];
  variants: Variant[];
  seller: Seller;
}

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  product$: Observable<Product | null>;
  reviews$: Observable<Review[]>;
  relatedProducts$: Observable<Product[]>;
  
  // Seçilen görsel
  selectedImage: string | null = null;
  
  // Seçilen varyant
  selectedVariant: Variant | null = null;
  
  // Adet
  quantity: number = 1;
  
  // Math nesnesi
  Math = Math;
  
  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {
    // Ürün detaylarını yükle
    this.product$ = this.route.paramMap.pipe(
      switchMap(params => {
        const productId = params.get('id');
        return productId ? this.http.get<Product>(`${environment.apiUrl}/products/${productId}`) : of(null);
      }),
      tap(product => {
        if (product) {
          // Ürün değerlendirmelerini yükle
          this.loadReviews(product.id);
          
          // Benzer ürünleri yükle
          this.loadRelatedProducts(product.id, product.seller.id);
        }
      }),
      catchError(error => {
        console.error('Ürün yüklenirken hata oluştu:', error);
        return of(null);
      })
    );
    
    // Default değerler
    this.reviews$ = of([]);
    this.relatedProducts$ = of([]);
  }
  
  ngOnInit(): void {}
  
  loadReviews(productId: number): void {
    this.reviews$ = this.http.get<Review[]>(`${environment.apiUrl}/products/${productId}/reviews`);
  }
  
  loadRelatedProducts(productId: number, sellerId: number): void {
    this.relatedProducts$ = this.http.get<Product[]>(`${environment.apiUrl}/products`, {
      params: {
        sellerId: sellerId.toString(),
        excludeProductId: productId.toString(),
        limit: '4'
      }
    });
  }
  
  selectVariant(variant: Variant): void {
    this.selectedVariant = variant;
  }
  
  increaseQuantity(): void {
    if (this.quantity < 10) {
      this.quantity++;
    }
  }
  
  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  
  addToCart(): void {
    // Sepete ekleme işlemi burada gerçekleştirilecek
    const productToAdd = {
      productId: this.product$.pipe(tap(product => product?.id)),
      variantId: this.selectedVariant?.id,
      quantity: this.quantity
    };
    
    this.http.post(`${environment.apiUrl}/cart/add`, productToAdd)
      .subscribe({
        next: () => {
          // Başarılı mesajı gösterme
          alert('Ürün sepete eklendi!');
        },
        error: (error) => {
          console.error('Sepete ekleme hatası:', error);
          alert('Ürün sepete eklenirken bir hata oluştu.');
        }
      });
  }
  
  addToWishlist(): void {
    // Favorilere ekleme işlemi burada gerçekleştirilecek
    this.product$.subscribe(product => {
      if (product) {
        this.http.post(`${environment.apiUrl}/wishlist/add`, { productId: product.id })
          .subscribe({
            next: () => {
              alert('Ürün favorilere eklendi!');
            },
            error: (error) => {
              console.error('Favorilere ekleme hatası:', error);
              alert('Ürün favorilere eklenirken bir hata oluştu.');
            }
          });
      }
    });
  }
}
