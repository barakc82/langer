import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminWordComponent } from './admin-word.component';

describe('AdminWordComponent', () => {
  let component: AdminWordComponent;
  let fixture: ComponentFixture<AdminWordComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminWordComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminWordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
