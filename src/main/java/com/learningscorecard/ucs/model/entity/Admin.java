package com.learningscorecard.ucs.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
@Data
@SuperBuilder
@AllArgsConstructor
public class Admin extends User {
}