import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEpisodeComponent } from './admin-episode.component';

describe('AdminEpisodeComponent', () => {
  let component: AdminEpisodeComponent;
  let fixture: ComponentFixture<AdminEpisodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminEpisodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminEpisodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
