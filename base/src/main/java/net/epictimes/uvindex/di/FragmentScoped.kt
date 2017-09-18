package net.epictimes.uvindex.di

import javax.inject.Scope

@Scope
@kotlin.annotation.Retention
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class FragmentScoped