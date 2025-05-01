import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

interface Category {
  id: number;
  name: string;
  imageUrl: string;
}

interface Product {
  id: number;
  name: string;
  price: number;
  discountPrice?: number;
  rating: number;
  images: { imageUrl: string }[];
}

interface Seller {
  id: number;
  storeName: string;
  logoUrl: string;
  rating: number;
  totalReviews: number;
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  // Kategoriler
  mainCategories$: Observable<Category[]>;
  
  // Ürünler
  featuredProducts$: Observable<Product[]>;
  newProducts$: Observable<Product[]>;
  
  // Satıcılar
  featuredSellers$: Observable<Seller[]>;
  
  constructor(private http: HttpClient) {
    // Ana kategorileri yükle
    this.mainCategories$ = this.http.get<Category[]>(`${environment.apiUrl}/categories/main`);
    
    // Öne çıkan ürünleri yükle
    this.featuredProducts$ = this.http.get<Product[]>(`${environment.apiUrl}/products/featured`, {
      params: { limit: '8' }
    });
    
    // Yeni ürünleri yükle
    this.newProducts$ = this.http.get<Product[]>(`${environment.apiUrl}/products`, {
      params: { 
        sort: 'newest',
        limit: '8'
      }
    });
    
    // Öne çıkan satıcıları yükle
    this.featuredSellers$ = this.http.get<Seller[]>(`${environment.apiUrl}/sellers/featured`, {
      params: { limit: '6' }
    });
  }
  
  ngOnInit(): void {
    // Burada ek bir işlem yapmamıza gerek yok, constructor'da apileri çağırıyoruz
  }
}
