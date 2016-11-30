package com.kst.models

/**
  * Created by victorgarcia on 29/11/16.
  */
case class LendingClub(id: Long, loan_amnt: Float, funded_amnt: Float, grade: String, emp_length: String, home_ownership: String, annual_inc: Float,
                       loan_status: String, purpose: String, zip_code: String, addr_state: String, total_pymnt_inv: Float)
