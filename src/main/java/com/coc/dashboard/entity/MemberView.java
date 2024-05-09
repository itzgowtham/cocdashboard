package com.coc.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "MEMBER_VIEW")
@Data
public class MemberView {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
	@Column(name = "months")
    private String months;
	
	@Column(name = "active_members")
    private Long activeMembers;

	@Column(name = "statecode")
	private String statecode;
	
	@Column(name = "lob")
	private String lob;

}
